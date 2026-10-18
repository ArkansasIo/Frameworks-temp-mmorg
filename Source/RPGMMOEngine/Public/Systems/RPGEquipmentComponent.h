#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "Systems/RPGSystemTypes.h"
#include "RPGEquipmentComponent.generated.h"

UCLASS(ClassGroup=(RPG), BlueprintType, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGEquipmentComponent : public UActorComponent
{
    GENERATED_BODY()
public:
    UFUNCTION(BlueprintCallable) bool EquipItem(const FRPGItemRef& Item, ERPGEquipmentSlot Slot);
    UFUNCTION(BlueprintCallable) bool UnequipItem(ERPGEquipmentSlot Slot);
    UFUNCTION(BlueprintPure) bool HasEquipment(ERPGEquipmentSlot Slot) const;
protected:
    UPROPERTY(EditAnywhere,Replicated,BlueprintReadOnly) TMap<TEnumAsByte<ERPGEquipmentSlot>,FRPGItemRef> Equipped;
};
