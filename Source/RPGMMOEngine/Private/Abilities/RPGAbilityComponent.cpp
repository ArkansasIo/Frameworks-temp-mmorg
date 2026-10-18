#include "Abilities/RPGAbilityComponent.h"
#include "Combat/RPGCombatComponent.h"
#include "Net/UnrealNetwork.h"

URPGAbilityComponent::URPGAbilityComponent()
{
    PrimaryComponentTick.bCanEverTick = true;
    SetIsReplicatedByDefault(true);
}

bool URPGAbilityComponent::RegisterAbility(const FRPGAbilitySpec& Ability)
{
    if (Ability.AbilityId.IsNone()) return false;
    for (FRPGAbilitySpec& Existing : Abilities)
    {
        if (Existing.AbilityId == Ability.AbilityId)
        {
            Existing = Ability;
            return true;
        }
    }
    Abilities.Add(Ability);
    Cooldowns.FindOrAdd(Ability.AbilityId, 0.0f);
    return true;
}

bool URPGAbilityComponent::CanCast(FName AbilityId) const
{
    const float* Remaining = Cooldowns.Find(AbilityId);
    return Remaining == nullptr || *Remaining <= 0.0f;
}

bool URPGAbilityComponent::CastAbility(FName AbilityId, AActor* Target)
{
    if (!CanCast(AbilityId)) return false;

    const FRPGAbilitySpec* Spec = Abilities.FindByPredicate([AbilityId](const FRPGAbilitySpec& Item)
    {
        return Item.AbilityId == AbilityId;
    });
    if (!Spec) return false;
    if (Spec->Targeting != ERPGAbilityTargeting::Self && !Target) return false;

    if (Spec->Targeting != ERPGAbilityTargeting::Self)
    {
        if (URPGCombatComponent* Combat = Target->FindComponentByClass<URPGCombatComponent>())
        {
            Combat->ApplyDamage(Spec->Power, GetOwner());
        }
    }

    Cooldowns.FindOrAdd(AbilityId) = FMath::Max(0.0f, Spec->Cooldown);
    return true;
}

void URPGAbilityComponent::ResetCooldowns()
{
    for (TPair<FName, float>& Pair : Cooldowns) Pair.Value = 0.0f;
}

float URPGAbilityComponent::GetRemainingCooldown(FName AbilityId) const
{
    const float* Remaining = Cooldowns.Find(AbilityId);
    return Remaining ? FMath::Max(0.0f, *Remaining) : 0.0f;
}

void URPGAbilityComponent::TickComponent(float DeltaTime, ELevelTick TickType, FActorComponentTickFunction* ThisTickFunction)
{
    Super::TickComponent(DeltaTime, TickType, ThisTickFunction);
    for (TPair<FName, float>& Pair : Cooldowns)
    {
        Pair.Value = FMath::Max(0.0f, Pair.Value - DeltaTime);
    }
}

void URPGAbilityComponent::GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const
{
    Super::GetLifetimeReplicatedProps(OutLifetimeProps);
    DOREPLIFETIME(URPGAbilityComponent, Abilities);
}
