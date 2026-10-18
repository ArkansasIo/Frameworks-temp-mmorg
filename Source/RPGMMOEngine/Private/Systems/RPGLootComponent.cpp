#include "Systems/RPGLootComponent.h"
#include "Math/UnrealMathUtility.h"
TArray<FRPGLootEntry> URPGLootComponent::RollLoot() const { TArray<FRPGLootEntry> Result; for(const FRPGLootEntry& E:Entries){ if(FMath::FRand()<=E.DropChance){ FRPGLootEntry R=E; R.MinQuantity=FMath::RandRange(E.MinQuantity,E.MaxQuantity); R.MaxQuantity=R.MinQuantity; Result.Add(R); }} return Result; }
