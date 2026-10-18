#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "Systems/RPGSystemTypes.h"
#include "RPGInventoryComponent.generated.h"

UCLASS(ClassGroup=(RPG), BlueprintType, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGInventoryComponent : public UActorComponent
{
    GENERATED_BODY()
public:
    UFUNCTION(BlueprintCallable) bool AddItem(const FRPGItemRef& Item);
    UFUNCTION(BlueprintCallable) bool RemoveItem(FName ItemId,int32 Quantity);
    UFUNCTION(BlueprintPure) int32 GetItemCount(FName ItemId) const;
    UFUNCTION(BlueprintPure) const TArray<FRPGItemRef>& GetItems() const { return Items; }
protected:
    UPROPERTY(EditAnywhere,Replicated,BlueprintReadOnly) TArray<FRPGItemRef> Items;
};
