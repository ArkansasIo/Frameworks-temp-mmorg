#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "RPGLootComponent.generated.h"

USTRUCT(BlueprintType) struct RPGMMOENGINE_API FRPGLootEntry { GENERATED_BODY() UPROPERTY(EditAnywhere,BlueprintReadWrite) FName ItemId; UPROPERTY(EditAnywhere,BlueprintReadWrite) int32 MinQuantity=1; UPROPERTY(EditAnywhere,BlueprintReadWrite) int32 MaxQuantity=1; UPROPERTY(EditAnywhere,BlueprintReadWrite) float DropChance=1.f; };
UCLASS(ClassGroup=(RPG),BlueprintType,meta=(BlueprintSpawnableComponent)) class RPGMMOENGINE_API URPGLootComponent:public UActorComponent { GENERATED_BODY() public: UFUNCTION(BlueprintCallable) TArray<FRPGLootEntry> RollLoot() const; UPROPERTY(EditAnywhere,BlueprintReadWrite,Category="Loot") TArray<FRPGLootEntry> Entries; };
