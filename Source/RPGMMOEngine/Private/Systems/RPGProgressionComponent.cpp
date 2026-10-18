#include "Systems/RPGProgressionComponent.h"
#include "Math/UnrealMathUtility.h"

void URPGProgressionComponent::AddExperience(int64 Amount)
{
    if (Amount <= 0) return;
    Experience += Amount;
    while (Level < LevelCap && Experience >= GetRequiredExperience(Level + 1)) ++Level;
}

bool URPGProgressionComponent::AddLevel(int32 Amount)
{
    if (Amount <= 0) return false;
    const int32 Old = Level;
    Level = FMath::Clamp(Level + Amount, 1, LevelCap);
    return Level != Old;
}

int64 URPGProgressionComponent::GetRequiredExperience(int32 TargetLevel)
{
    if (TargetLevel <= 1) return 0;
    return FMath::FloorToInt64(FMath::Pow(static_cast<double>(TargetLevel), 2.5) * 500.0);
}
