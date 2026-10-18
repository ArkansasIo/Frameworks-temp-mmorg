#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "RPGMMOEngine.generated.h"

UCLASS(ClassGroup=(RPG), BlueprintType, Blueprintable, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGStatsComponent : public UActorComponent
{
    GENERATED_BODY()
public:
    URPGStatsComponent();
    UFUNCTION(BlueprintCallable) double GetStat(ERPGStatId Stat) const;
    UFUNCTION(BlueprintCallable) void SetBaseStat(ERPGStatId Stat, double Value);
    UFUNCTION(BlueprintCallable) void AddModifier(const FRPGStatModifier& Modifier);
    UFUNCTION(BlueprintCallable) void RemoveModifiersFromSource(FName SourceId);
    UFUNCTION(BlueprintCallable) void RecalculateStats();
protected:
    UPROPERTY(EditAnywhere, Replicated) TMap<ERPGStatId,double> BaseStats;
    UPROPERTY(Replicated) TArray<FRPGStatModifier> Modifiers;
    UPROPERTY(Replicated) TMap<ERPGStatId,double> EffectiveStats;
    virtual void GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const override;
};
