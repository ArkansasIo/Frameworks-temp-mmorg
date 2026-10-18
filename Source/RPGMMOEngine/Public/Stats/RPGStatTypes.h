#pragma once

#include "CoreMinimal.h"
#include "RPGStatTypes.generated.h"

UENUM(BlueprintType)
enum class ERPGStatCategory : uint8
{
    Primary, Offensive, Defensive, Precision, Penetration, Tempo, Recovery, Resource, Utility, PvE, PvP
};

UENUM(BlueprintType)
enum class ERPGModifierOperation : uint8
{
    AddFlat, AddPercent, Multiply, Override, Minimum, Maximum, Clamp
};

USTRUCT(BlueprintType)
struct RPGMMOENGINE_API FRPGStatValue
{
    GENERATED_BODY()

    UPROPERTY(EditAnywhere, BlueprintReadWrite) FName StatId;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) float BaseValue = 0.f;
    UPROPERTY(VisibleAnywhere, BlueprintReadOnly) float EffectiveValue = 0.f;
};

USTRUCT(BlueprintType)
struct RPGMMOENGINE_API FRPGStatModifier
{
    GENERATED_BODY()

    UPROPERTY(EditAnywhere, BlueprintReadWrite) FName StatId;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) ERPGModifierOperation Operation = ERPGModifierOperation::AddFlat;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) float Value = 0.f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) int32 Priority = 0;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) FName SourceId;
};
