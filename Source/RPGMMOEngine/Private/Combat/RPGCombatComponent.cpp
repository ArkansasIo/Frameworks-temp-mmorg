#include "Combat/RPGCombatComponent.h"
#include "Net/UnrealNetwork.h"

URPGCombatComponent::URPGCombatComponent()
{
    PrimaryComponentTick.bCanEverTick = false;
    SetIsReplicatedByDefault(true);
}

void URPGCombatComponent::BeginPlay()
{
    Super::BeginPlay();
    CurrentHealth = MaxHealth;
}

float URPGCombatComponent::ApplyDamage(float RawDamage, AActor* Source)
{
    if (!IsAlive() || RawDamage <= 0.0f)
    {
        return 0.0f;
    }

    const float Applied = FMath::Min(CurrentHealth, RawDamage);
    CurrentHealth = FMath::Clamp(CurrentHealth - Applied, 0.0f, MaxHealth);
    if (Source)
    {
        OnDamageTaken.Broadcast(Source, Applied);
    }
    if (CurrentHealth <= 0.0f)
    {
        StopCombat();
    }
    return Applied;
}

void URPGCombatComponent::Heal(float Amount)
{
    if (Amount <= 0.0f || !IsAlive())
    {
        return;
    }
    CurrentHealth = FMath::Clamp(CurrentHealth + Amount, 0.0f, MaxHealth);
}

void URPGCombatComponent::StartCombat(AActor* Opponent)
{
    if (!Opponent || Opponent == GetOwner() || !IsAlive())
    {
        return;
    }
    CombatTarget = Opponent;
    if (!bInCombat)
    {
        bInCombat = true;
        OnCombatStateChanged.Broadcast(true);
    }
}

void URPGCombatComponent::StopCombat()
{
    CombatTarget = nullptr;
    if (bInCombat)
    {
        bInCombat = false;
        OnCombatStateChanged.Broadcast(false);
    }
}

float URPGCombatComponent::GetHealthPercent() const
{
    return MaxHealth > 0.0f ? CurrentHealth / MaxHealth : 0.0f;
}

void URPGCombatComponent::GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const
{
    Super::GetLifetimeReplicatedProps(OutLifetimeProps);
    DOREPLIFETIME(URPGCombatComponent, MaxHealth);
    DOREPLIFETIME(URPGCombatComponent, CurrentHealth);
    DOREPLIFETIME(URPGCombatComponent, bInCombat);
    DOREPLIFETIME(URPGCombatComponent, CombatTarget);
}
