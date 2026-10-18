#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "RPGShopComponent.generated.h"

USTRUCT(BlueprintType)
struct RPGMMOENGINE_API FRPGShopEntry
{
    GENERATED_BODY()
    UPROPERTY(EditAnywhere, BlueprintReadWrite) FName ItemId;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) int32 Quantity = 0;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) int64 BuyPrice = 0;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) int64 SellPrice = 0;
};

UCLASS(ClassGroup=(RPG), BlueprintType, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGShopComponent : public UActorComponent
{
    GENERATED_BODY()
public:
    UFUNCTION(BlueprintCallable, Category="RPG|Economy") bool Buy(FName ItemId, int32 Quantity, int64& TotalCost);
    UFUNCTION(BlueprintCallable, Category="RPG|Economy") bool Sell(FName ItemId, int32 Quantity, int64& TotalValue);
    UFUNCTION(BlueprintPure, Category="RPG|Economy") bool HasStock(FName ItemId, int32 Quantity = 1) const;
protected:
    UPROPERTY(EditAnywhere, BlueprintReadOnly, Category="RPG|Economy") TArray<FRPGShopEntry> Entries;
};
