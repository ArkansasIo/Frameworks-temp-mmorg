#include "Systems/RPGShopComponent.h"

bool URPGShopComponent::Buy(FName ItemId, int32 Quantity, int64& TotalCost)
{
    TotalCost = 0;
    if (ItemId.IsNone() || Quantity <= 0) return false;
    for (FRPGShopEntry& Entry : Entries) if (Entry.ItemId == ItemId && Entry.Quantity >= Quantity && Entry.BuyPrice >= 0) { TotalCost = Entry.BuyPrice * static_cast<int64>(Quantity); Entry.Quantity -= Quantity; return true; }
    return false;
}

bool URPGShopComponent::Sell(FName ItemId, int32 Quantity, int64& TotalValue)
{
    TotalValue = 0;
    if (ItemId.IsNone() || Quantity <= 0) return false;
    for (const FRPGShopEntry& Entry : Entries) if (Entry.ItemId == ItemId && Entry.SellPrice >= 0) { TotalValue = Entry.SellPrice * static_cast<int64>(Quantity); return true; }
    return false;
}

bool URPGShopComponent::HasStock(FName ItemId, int32 Quantity) const
{
    if (Quantity <= 0) return false;
    for (const FRPGShopEntry& Entry : Entries) if (Entry.ItemId == ItemId) return Entry.Quantity >= Quantity;
    return false;
}
