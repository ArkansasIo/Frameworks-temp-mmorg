#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "Systems/RPGSystemTypes.h"
#include "RPGEffectComponent.generated.h"

UCLASS(ClassGroup=(RPG), BlueprintType, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGEffectComponent : public UActorComponent
{
    GENERATED_BODY()
public:
    UFUNCTION(BlueprintCallable) void ApplyEffect(const FRPGEffectSpec& Spec);
    UFUNCTION(BlueprintCallable) void RemoveEffect(FName EffectId);
    UFUNCTION(BlueprintPure) bool HasEffect(FName EffectId) const;
    UFUNCTION(BlueprintPure) const TArray<FRPGEffectSpec>& GetEffects() const { return Effects; }
protected:
    UPROPERTY(EditAnywhere,Replicated,BlueprintReadOnly) TArray<FRPGEffectSpec> Effects;
};
