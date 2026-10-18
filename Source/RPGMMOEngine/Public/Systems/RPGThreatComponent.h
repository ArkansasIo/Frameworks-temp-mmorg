#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "RPGThreatComponent.generated.h"

USTRUCT(BlueprintType) struct RPGMMOENGINE_API FRPGThreatEntry { GENERATED_BODY() UPROPERTY(BlueprintReadOnly) TObjectPtr<AActor> Target; UPROPERTY(BlueprintReadOnly) float Threat=0; };

UCLASS(ClassGroup=(RPG), BlueprintType, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGThreatComponent : public UActorComponent
{
    GENERATED_BODY()
public:
    UFUNCTION(BlueprintCallable) void AddThreat(AActor* Target,float Amount);
    UFUNCTION(BlueprintCallable) void ClearThreat(AActor* Target);
    UFUNCTION(BlueprintPure) AActor* GetHighestThreatTarget() const;
    UFUNCTION(BlueprintPure) float GetThreat(AActor* Target) const;
protected:
    UPROPERTY(Replicated,BlueprintReadOnly) TArray<FRPGThreatEntry> ThreatTable;
};
