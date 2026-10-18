#include "Systems/RPGEquipmentComponent.h"
#include "Net/UnrealNetwork.h"

URPGEquipmentComponent::URPGEquipmentComponent()
{
    SetIsReplicatedByDefault(true);
}

bool URPGEquipmentComponent::EquipItem(const FRPGItemRef& Item, ERPGEquipmentSlot Slot)
{
    if (Item.ItemId.IsNone() || Item.Quantity <= 0) return false;
    Equipped.FindOrAdd(TEnumAsByte<ERPGEquipmentSlot>(Slot)) = Item;
    return true;
}

bool URPGEquipmentComponent::UnequipItem(ERPGEquipmentSlot Slot)
{
    return Equipped.Remove(TEnumAsByte<ERPGEquipmentSlot>(Slot)) > 0;
}

bool URPGEquipmentComponent::HasEquipment(ERPGEquipmentSlot Slot) const
{
    return Equipped.Contains(TEnumAsByte<ERPGEquipmentSlot>(Slot));
}

void URPGEquipmentComponent::GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const
{
    Super::GetLifetimeReplicatedProps(OutLifetimeProps);
    DOREPLIFETIME(URPGEquipmentComponent, Equipped);
}
