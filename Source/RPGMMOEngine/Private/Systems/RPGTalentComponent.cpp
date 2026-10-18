#include "Systems/RPGTalentComponent.h"
#include "Net/UnrealNetwork.h"

bool URPGTalentComponent::UnlockTalent(FName TalentId)
{
    if (TalentId.IsNone()) return false;
    for (FRPGTalentNode& Talent : Talents)
    {
        if (Talent.TalentId != TalentId || Talent.bUnlocked) continue;
        if (!Talent.ParentTalentId.IsNone() && !IsTalentUnlocked(Talent.ParentTalentId)) return false;
        if (Talent.Cost <= 0 || TalentPoints < Talent.Cost) return false;
        TalentPoints -= Talent.Cost;
        Talent.bUnlocked = true;
        return true;
    }
    return false;
}

void URPGTalentComponent::RegisterTalent(const FRPGTalentNode& Talent)
{
    if (Talent.TalentId.IsNone()) return;
    for (FRPGTalentNode& Existing : Talents)
    {
        if (Existing.TalentId == Talent.TalentId) { Existing = Talent; return; }
    }
    Talents.Add(Talent);
}

bool URPGTalentComponent::IsTalentUnlocked(FName TalentId) const
{
    for (const FRPGTalentNode& Talent : Talents) if (Talent.TalentId == TalentId) return Talent.bUnlocked;
    return false;
}

void URPGTalentComponent::AddTalentPoints(int32 Amount)
{
    if (Amount > 0) TalentPoints += Amount;
}

void URPGTalentComponent::GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const
{
    Super::GetLifetimeReplicatedProps(OutLifetimeProps);
    DOREPLIFETIME(URPGTalentComponent, Talents);
    DOREPLIFETIME(URPGTalentComponent, TalentPoints);
}
