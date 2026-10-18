#pragma once

#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "Abilities/RPGAbilityTypes.h"
#include "RPGAbilityComponent.generated.h"

UCLASS(ClassGroup=(RPG), BlueprintType, Blueprintable, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGAbilityComponent : public UActorComponent
{
    GENERATED_BODY()

public:
    URPGAbilityComponent();

    UFUNCTION(BlueprintCallable, Category="RPG|Abilities") bool RegisterAbility(const FRPGAbilitySpec& Ability);
    UFUNCTION(BlueprintCallable, Category="RPG|Abilities") bool CanCast(FName AbilityId) const;
    UFUNCTION(BlueprintCallable, Category="RPG|Abilities") bool CastAbility(FName AbilityId, AActor* Target);
    UFUNCTION(BlueprintCallable, Category="RPG|Abilities") void ResetCooldowns();
    UFUNCTION(BlueprintPure, Category="RPG|Abilities") float GetRemainingCooldown(FName AbilityId) const;

protected:
    UPROPERTY(EditAnywhere, Replicated, BlueprintReadOnly, Category="RPG|Abilities") TArray<FRPGAbilitySpec> Abilities;
    UPROPERTY(VisibleInstanceOnly, BlueprintReadOnly, Category="RPG|Abilities") TMap<FName, float> Cooldowns;

    virtual void GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const override;
};
