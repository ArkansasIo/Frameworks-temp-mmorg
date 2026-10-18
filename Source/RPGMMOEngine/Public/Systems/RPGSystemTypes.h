#pragma once
#include "CoreMinimal.h"
#include "Systems/RPGSystemTypes.generated.h"

UENUM(BlueprintType) enum class ERPGResourceType:uint8 { HP, MP, Stamina, Rage, Energy, Focus, Faith, Aether, Heat, Sanity, Resolve, ComboPoints, Charges, Ammunition };
UENUM(BlueprintType) enum class ERPGItemRarity:uint8 { Common, Uncommon, Rare, Epic, Legendary, Mythic };
UENUM(BlueprintType) enum class ERPGEquipmentSlot:uint8 { Head, Chest, Legs, Gloves, Boots, MainHand, OffHand, Ring1, Ring2, Amulet, Cloak, Belt, Trinket1, Trinket2, Relic, Artifact, Mount, Companion, Cosmetic, Augment };
UENUM(BlueprintType) enum class ERPGCrowdControl:uint8 { None, Stun, Freeze, Root, Silence, Fear, Charm, Sleep, Knockback, Knockdown, Disarm, Taunt, Slow, Cripple, Curse, Hex };

USTRUCT(BlueprintType) struct RPGMMOENGINE_API FRPGResourceValue { GENERATED_BODY() UPROPERTY(EditAnywhere,BlueprintReadWrite) ERPGResourceType Type=ERPGResourceType::HP; UPROPERTY(EditAnywhere,BlueprintReadWrite) float Current=0; UPROPERTY(EditAnywhere,BlueprintReadWrite) float Maximum=0; };
USTRUCT(BlueprintType) struct RPGMMOENGINE_API FRPGItemRef { GENERATED_BODY() UPROPERTY(EditAnywhere,BlueprintReadWrite) FName ItemId; UPROPERTY(EditAnywhere,BlueprintReadWrite) int32 Quantity=1; UPROPERTY(EditAnywhere,BlueprintReadWrite) int32 Level=1; };
USTRUCT(BlueprintType) struct RPGMMOENGINE_API FRPGEffectSpec { GENERATED_BODY() UPROPERTY(EditAnywhere,BlueprintReadWrite) FName EffectId; UPROPERTY(EditAnywhere,BlueprintReadWrite) float Magnitude=0; UPROPERTY(EditAnywhere,BlueprintReadWrite) float Duration=0; UPROPERTY(EditAnywhere,BlueprintReadWrite) int32 Stacks=1; };
