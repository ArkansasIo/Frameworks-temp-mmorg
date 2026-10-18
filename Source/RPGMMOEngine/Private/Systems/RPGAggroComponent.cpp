#include "Systems/RPGAggroComponent.h"
#include "Net/UnrealNetwork.h"

void URPGAggroComponent::AddThreat(AActor* Target, float Amount)
{
    if (!Target || Amount <= 0.f) return;
    for (FRPGAggroEntry& Entry : ThreatTable)
    {
        if (Entry.Target == Target) { Entry.Threat += Amount; return; }
    }
    FRPGAggroEntry Entry; Entry.Target = Target; Entry.Threat = Amount; ThreatTable.Add(Entry);
}

void URPGAggroComponent::RemoveTarget(AActor* Target)
{
    ThreatTable.RemoveAll([Target](const FRPGAggroEntry& Entry){ return Entry.Target == Target; });
}

void URPGAggroComponent::ClearAggro() { ThreatTable.Reset(); }

AActor* URPGAggroComponent::GetTopThreat() const
{
    AActor* Best = nullptr; float BestThreat = -1.f;
    for (const FRPGAggroEntry& Entry : ThreatTable)
    {
        if (IsValid(Entry.Target) && Entry.Threat > BestThreat) { Best = Entry.Target; BestThreat = Entry.Threat; }
    }
    return Best;
}

float URPGAggroComponent::GetThreat(AActor* Target) const
{
    for (const FRPGAggroEntry& Entry : ThreatTable) if (Entry.Target == Target) return Entry.Threat;
    return 0.f;
}

void URPGAggroComponent::GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const
{
    Super::GetLifetimeReplicatedProps(OutLifetimeProps);
    DOREPLIFETIME(URPGAggroComponent, ThreatTable);
}
