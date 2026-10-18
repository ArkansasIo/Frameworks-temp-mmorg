#include "Systems/RPGSkillComponent.h"

bool URPGSkillComponent::LearnSkill(FName SkillId, int32 Rank)
{
    if (SkillId.IsNone() || Rank <= 0) return false;
    if (GetSkillRank(SkillId) > 0) return AddSkillRank(SkillId, Rank);
    FRPGSkillState Skill; Skill.SkillId = SkillId; Skill.Rank = FMath::Clamp(Rank, 1, Skill.MaxRank); Skills.Add(Skill); return true;
}

bool URPGSkillComponent::AddSkillRank(FName SkillId, int32 Amount)
{
    if (SkillId.IsNone() || Amount <= 0) return false;
    for (FRPGSkillState& Skill : Skills) if (Skill.SkillId == SkillId) { const int32 Old = Skill.Rank; Skill.Rank = FMath::Clamp(Skill.Rank + Amount, 0, Skill.MaxRank); return Skill.Rank != Old; }
    return LearnSkill(SkillId, Amount);
}

int32 URPGSkillComponent::GetSkillRank(FName SkillId) const
{
    for (const FRPGSkillState& Skill : Skills) if (Skill.SkillId == SkillId) return Skill.Rank;
    return 0;
}

bool URPGSkillComponent::HasSkill(FName SkillId, int32 RequiredRank) const { return RequiredRank > 0 && GetSkillRank(SkillId) >= RequiredRank; }
