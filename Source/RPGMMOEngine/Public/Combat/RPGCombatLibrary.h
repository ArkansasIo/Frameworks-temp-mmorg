#pragma once
#include "CoreMinimal.h"
#include "Kismet/BlueprintFunctionLibrary.h"
#include "RPGMMOEngine.generated.h"

UENUM(BlueprintType) enum class ERPGDamageType:uint8 { Physical, Fire, Ice, Lightning, Earth, Wind, Water, Light, Dark, Arcane, Poison, Bleed, Void, True };
USTRUCT(BlueprintType) struct RPGMMOENGINE_API FRPGDamagePacket { GENERATED_BODY() UPROPERTY(BlueprintReadWrite) double RawDamage=0; UPROPERTY(BlueprintReadWrite) ERPGDamageType DamageType=ERPGDamageType::Physical; UPROPERTY(BlueprintReadWrite) double Penetration=0; UPROPERTY(BlueprintReadWrite) bool CanCrit=true; };
USTRUCT(BlueprintType) struct RPGMMOENGINE_API FRPGDamageResult { GENERATED_BODY() UPROPERTY(BlueprintReadOnly) double FinalDamage=0; UPROPERTY(BlueprintReadOnly) bool Hit=false; UPROPERTY(BlueprintReadOnly) bool Critical=false; };
UCLASS() class RPGMMOENGINE_API URPGCombatLibrary:public UBlueprintFunctionLibrary { GENERATED_BODY() public: UFUNCTION(BlueprintPure) static double Mitigation(double Armor,double LevelK); UFUNCTION(BlueprintPure) static FRPGDamageResult ResolveDamage(FRPGDamagePacket Packet,double Armor,double Resist,double CritChance,double CritMultiplier,double Accuracy,double Evasion,double LevelK); };
