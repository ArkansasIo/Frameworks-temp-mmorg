#include "Systems/RPGFactionComponent.h"

void URPGFactionComponent::SetStanding(FName FactionId, int32 Standing)
{
    if (FactionId.IsNone()) return;
    for (FRPGFactionStanding& Entry : Standings) if (Entry.FactionId == FactionId) { Entry.Standing = Standing; return; }
    FRPGFactionStanding Entry; Entry.FactionId = FactionId; Entry.Standing = Standing; Standings.Add(Entry);
}

void URPGFactionComponent::ModifyStanding(FName FactionId, int32 Delta) { SetStanding(FactionId, GetStanding(FactionId) + Delta); }

int32 URPGFactionComponent::GetStanding(FName FactionId) const
{
    for (const FRPGFactionStanding& Entry : Standings) if (Entry.FactionId == FactionId) return Entry.Standing;
    return 0;
}

bool URPGFactionComponent::IsHostile(FName FactionId, int32 HostileThreshold) const { return GetStanding(FactionId) <= HostileThreshold; }
