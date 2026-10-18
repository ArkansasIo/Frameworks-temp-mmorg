#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "RPGProgressionComponent.generated.h"

UCLASS(ClassGroup=(RPG), BlueprintType, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGProgressionComponent : public UActorComponent
{
    GENERATED_BODY()
public:
    URPGProgressionComponent();
    UFUNCTION(BlueprintCallable) void AddExperience(int64 Amount);
    UFUNCTION(BlueprintCallable) bool AddLevel(int32 Amount);
    UFUNCTION(BlueprintPure) int32 GetLevel() const { return Level; }
    UFUNCTION(BlueprintPure) int64 GetExperience() const { return Experience; }
    UFUNCTION(BlueprintPure) static int64 GetRequiredExperience(int32 TargetLevel);
protected:
    UPROPERTY(EditAnywhere,Replicated,BlueprintReadOnly) int32 Level=1;
    UPROPERTY(EditAnywhere,Replicated,BlueprintReadOnly) int64 Experience=0;
    UPROPERTY(EditAnywhere,BlueprintReadOnly) int32 LevelCap=100;
    virtual void GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const override;
};
