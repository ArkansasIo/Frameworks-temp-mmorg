#include "Systems/RPGThreatComponent.h"
#include "Net/UnrealNetwork.h"

URPGThreatComponent::URPGThreatComponent()
{
    SetIsReplicatedByDefault(true);
}

void URPGThreatComponent::AddThreat(AActor* Target, float Amount)
{
    if (!Target || Amount <= 0.0f) return;
    for (FRPGThreatEntry& Entry : ThreatTable)
    {
        if (Entry.Target == Target)
        {
            Entry.Threat += Amount;
            return;
        }
    }
    FRPGThreatEntry Entry;
    Entry.Target = Target;
    Entry.Threat = Amount;
    ThreatTable.Add(Entry);
}

void URPGThreatComponent::ClearThreat(AActor* Target)
{
    ThreatTable.RemoveAll([Target](const FRPGThreatEntry& Entry)
    {
        return Entry.Target == Target;
    });
}

AActor* URPGThreatComponent::GetHighestThreatTarget() const
{
    AActor* BestTarget = nullptr;
    float BestThreat = -1.0f;
    for (const FRPGThreatEntry& Entry : ThreatTable)
    {
        if (Entry.Target && Entry.Threat > BestThreat)
        {
            BestTarget = Entry.Target;
            BestThreat = Entry.Threat;
        }
    }
    return BestTarget;
}

float URPGThreatComponent::GetThreat(AActor* Target) const
{
    if (!Target) return 0.0f;
    for (const FRPGThreatEntry& Entry : ThreatTable)
    {
        if (Entry.Target == Target) return Entry.Threat;
    }
    return 0.0f;
}

void URPGThreatComponent::GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const
{
    Super::GetLifetimeReplicatedProps(OutLifetimeProps);
    DOREPLIFETIME(URPGThreatComponent, ThreatTable);
}
