#pragma once
#include "CoreMinimal.h"
#include "UObject/Object.h"
#include "RPGMMOEngine.generated.h"

UENUM(BlueprintType)
enum class ERPGStatId : uint8 { STR, DEX, CON, INT, WIS, VIT, SPI, LCK, WIL, CHA, PhysicalPower, SpellPower, Armor, MagicResistance, CritChance, CritDamage, Accuracy, Evasion, Haste, Tenacity, MaxHP, MaxMP, MaxStamina };

UENUM(BlueprintType)
enum class ERPGModifierOperation : uint8 { AddFlat, AddPercent, Multiply, Override, Minimum, Maximum };

USTRUCT(BlueprintType)
struct RPGMMOENGINE_API FRPGStatModifier
{
    GENERATED_BODY()
    UPROPERTY(EditAnywhere, BlueprintReadWrite) ERPGStatId Stat = ERPGStatId::STR;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) ERPGModifierOperation Operation = ERPGModifierOperation::AddFlat;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) double Value = 0.0;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) int32 Priority = 0;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) FName SourceId;
};
