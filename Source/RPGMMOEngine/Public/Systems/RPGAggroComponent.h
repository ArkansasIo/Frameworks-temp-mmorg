#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "RPGAggroComponent.generated.h"

USTRUCT(BlueprintType)
struct RPGMMOENGINE_API FRPGAggroEntry
{
    GENERATED_BODY()
    UPROPERTY(BlueprintReadOnly) TObjectPtr<AActor> Target;
    UPROPERTY(BlueprintReadOnly) float Threat = 0.f;
};

UCLASS(ClassGroup=(RPG), BlueprintType, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGAggroComponent : public UActorComponent
{
    GENERATED_BODY()
public:
    UFUNCTION(BlueprintCallable, Category="RPG|AI") void AddThreat(AActor* Target, float Amount);
    UFUNCTION(BlueprintCallable, Category="RPG|AI") void RemoveTarget(AActor* Target);
    UFUNCTION(BlueprintCallable, Category="RPG|AI") void ClearAggro();
    UFUNCTION(BlueprintPure, Category="RPG|AI") AActor* GetTopThreat() const;
    UFUNCTION(BlueprintPure, Category="RPG|AI") float GetThreat(AActor* Target) const;
protected:
    UPROPERTY(Replicated, BlueprintReadOnly, Category="RPG|AI") TArray<FRPGAggroEntry> ThreatTable;
    virtual void GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const override;
};
