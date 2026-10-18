#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "RPGPvPComponent.generated.h"

UENUM(BlueprintType)
enum class ERPGPvPState : uint8 { Peaceful, Flagged, InCombat, DuelPending, DuelActive };

UCLASS(ClassGroup=(RPG), BlueprintType, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGPvPComponent : public UActorComponent
{
    GENERATED_BODY()
public:
    UFUNCTION(BlueprintCallable) void SetPvPEnabled(bool bEnabled);
    UFUNCTION(BlueprintPure) bool IsPvPEnabled() const { return PvPState != ERPGPvPState::Peaceful; }
    UFUNCTION(BlueprintCallable) bool RequestDuel(AActor* Target);
    UFUNCTION(BlueprintCallable) void AcceptDuel();
    UFUNCTION(BlueprintCallable) void CancelDuel();
    UFUNCTION(BlueprintPure) AActor* GetDuelTarget() const { return DuelTarget; }
    UFUNCTION(BlueprintPure) ERPGPvPState GetPvPState() const { return PvPState; }
protected:
    UPROPERTY(Replicated, BlueprintReadOnly) ERPGPvPState PvPState = ERPGPvPState::Peaceful;
    UPROPERTY(Replicated, BlueprintReadOnly) TObjectPtr<AActor> DuelTarget;
    virtual void GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const override;
};
