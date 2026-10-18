#include "Systems/RPGEffectComponent.h"

void URPGEffectComponent::ApplyEffect(const FRPGEffectSpec& Spec)
{
    if (Spec.EffectId.IsNone()) return;
    for (FRPGEffectSpec& Existing : Effects) if (Existing.EffectId == Spec.EffectId) { Existing.Stacks += Spec.Stacks; Existing.Magnitude = Spec.Magnitude; Existing.Duration = Spec.Duration; return; }
    Effects.Add(Spec);
}
void URPGEffectComponent::RemoveEffect(FName EffectId) { Effects.RemoveAll([&](const FRPGEffectSpec& E){return E.EffectId==EffectId;}); }
bool URPGEffectComponent::HasEffect(FName EffectId) const { return Effects.ContainsByPredicate([&](const FRPGEffectSpec& E){return E.EffectId==EffectId;}); }
