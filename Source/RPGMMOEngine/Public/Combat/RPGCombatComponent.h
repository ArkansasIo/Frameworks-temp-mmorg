#pragma once

#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "Combat/RPGCombatTypes.h"
#include "RPGCombatComponent.generated.h"

DECLARE_DYNAMIC_MULTICAST_DELEGATE_TwoParams(FRPGDamageEvent, AActor*, Source, float, Amount);
DECLARE_DYNAMIC_MULTICAST_DELEGATE_OneParam(FRPGCombatStateEvent, bool, bInCombat);

UCLASS(ClassGroup=(RPG), BlueprintType, Blueprintable, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGCombatComponent : public UActorComponent
{
    GENERATED_BODY()

public:
    URPGCombatComponent();

    UFUNCTION(BlueprintCallable, Category="RPG|Combat") float ApplyDamage(float RawDamage, AActor* Source);
    UFUNCTION(BlueprintCallable, Category="RPG|Combat") void Heal(float Amount);
    UFUNCTION(BlueprintCallable, Category="RPG|Combat") void StartCombat(AActor* Opponent);
    UFUNCTION(BlueprintCallable, Category="RPG|Combat") void StopCombat();
    UFUNCTION(BlueprintPure, Category="RPG|Combat") bool IsAlive() const { return CurrentHealth > 0.0f; }
    UFUNCTION(BlueprintPure, Category="RPG|Combat") float GetHealth() const { return CurrentHealth; }
    UFUNCTION(BlueprintPure, Category="RPG|Combat") float GetHealthPercent() const;
    UFUNCTION(BlueprintPure, Category="RPG|Combat") AActor* GetCombatTarget() const { return CombatTarget.Get(); }

    UPROPERTY(BlueprintAssignable, Category="RPG|Combat") FRPGDamageEvent OnDamageTaken;
    UPROPERTY(BlueprintAssignable, Category="RPG|Combat") FRPGCombatStateEvent OnCombatStateChanged;

protected:
    UPROPERTY(EditAnywhere, Replicated, BlueprintReadOnly, Category="RPG|Combat") float MaxHealth = 100.0f;
    UPROPERTY(VisibleInstanceOnly, Replicated, BlueprintReadOnly, Category="RPG|Combat") float CurrentHealth = 100.0f;
    UPROPERTY(VisibleInstanceOnly, Replicated, BlueprintReadOnly, Category="RPG|Combat") bool bInCombat = false;
    UPROPERTY(VisibleInstanceOnly, Replicated, BlueprintReadOnly, Category="RPG|Combat") TObjectPtr<AActor> CombatTarget;

    virtual void BeginPlay() override;
    virtual void GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const override;
};
