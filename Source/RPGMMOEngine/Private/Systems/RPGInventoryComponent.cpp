#include "Systems/RPGInventoryComponent.h"
#include "Net/UnrealNetwork.h"

URPGInventoryComponent::URPGInventoryComponent()
{
    SetIsReplicatedByDefault(true);
}

bool URPGInventoryComponent::AddItem(const FRPGItemRef& Item)
{
    if (Item.ItemId.IsNone() || Item.Quantity <= 0) return false;
    for (FRPGItemRef& Existing : Items)
    {
        if (Existing.ItemId == Item.ItemId && Existing.Level == Item.Level)
        {
            Existing.Quantity += Item.Quantity;
            return true;
        }
    }
    Items.Add(Item);
    return true;
}

bool URPGInventoryComponent::RemoveItem(FName ItemId, int32 Quantity)
{
    if (Quantity <= 0) return false;
    for (int32 i = 0; i < Items.Num(); ++i)
    {
        if (Items[i].ItemId == ItemId)
        {
            if (Items[i].Quantity < Quantity) return false;
            Items[i].Quantity -= Quantity;
            if (Items[i].Quantity == 0) Items.RemoveAt(i);
            return true;
        }
    }
    return false;
}

int32 URPGInventoryComponent::GetItemCount(FName ItemId) const
{
    int32 Total = 0;
    for (const FRPGItemRef& Item : Items)
    {
        if (Item.ItemId == ItemId) Total += Item.Quantity;
    }
    return Total;
}

void URPGInventoryComponent::GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const
{
    Super::GetLifetimeReplicatedProps(OutLifetimeProps);
    DOREPLIFETIME(URPGInventoryComponent, Items);
}
