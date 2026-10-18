#pragma once

#include "CoreMinimal.h"
#include "RPGAbilityTypes.generated.h"

UENUM(BlueprintType)
enum class ERPGAbilityTargeting : uint8
{
    Self,
    SingleTarget,
    Area,
    Cone,
    Line
};

USTRUCT(BlueprintType)
struct RPGMMOENGINE_API FRPGAbilitySpec
{
    GENERATED_BODY()

    UPROPERTY(EditAnywhere, BlueprintReadWrite) FName AbilityId;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) float Cooldown = 1.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) float ResourceCost = 0.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) float Power = 10.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) float Range = 300.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) ERPGAbilityTargeting Targeting = ERPGAbilityTargeting::SingleTarget;
};
