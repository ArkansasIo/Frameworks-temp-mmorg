#pragma once

#include "CoreMinimal.h"
#include "RPGCombatTypes.generated.h"

UENUM(BlueprintType)
enum class ERPGDamageType : uint8
{
    Physical, Slash, Pierce, Blunt, Fire, Ice, Lightning, Earth, Wind, Water, Light, Dark, Arcane, Poison, Bleed, Burn, Frost, Shock, Corruption, Void, Chaos, True
};

USTRUCT(BlueprintType)
struct RPGMMOENGINE_API FRPGDamagePacket
{
    GENERATED_BODY()
    UPROPERTY(BlueprintReadWrite) float RawDamage = 0.f;
    UPROPERTY(BlueprintReadWrite) ERPGDamageType DamageType = ERPGDamageType::Physical;
    UPROPERTY(BlueprintReadWrite) float ArmorPenetration = 0.f;
    UPROPERTY(BlueprintReadWrite) float MagicPenetration = 0.f;
    UPROPERTY(BlueprintReadWrite) bool bCanCriticalStrike = true;
    UPROPERTY(BlueprintReadWrite) FName AbilityId;
};

USTRUCT(BlueprintType)
struct RPGMMOENGINE_API FRPGDamageResult
{
    GENERATED_BODY()
    UPROPERTY(BlueprintReadOnly) float FinalDamage = 0.f;
    UPROPERTY(BlueprintReadOnly) bool bHit = true;
    UPROPERTY(BlueprintReadOnly) bool bCritical = false;
    UPROPERTY(BlueprintReadOnly) bool bBlocked = false;
};
