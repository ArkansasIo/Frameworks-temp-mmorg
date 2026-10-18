#include "Systems/RPGProgressionComponent.h"
#include "Math/UnrealMathUtility.h"
#include "Net/UnrealNetwork.h"

URPGProgressionComponent::URPGProgressionComponent()
{
    SetIsReplicatedByDefault(true);
}

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

void URPGProgressionComponent::GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const
{
    Super::GetLifetimeReplicatedProps(OutLifetimeProps);
    DOREPLIFETIME(URPGProgressionComponent, Level);
    DOREPLIFETIME(URPGProgressionComponent, Experience);
}
