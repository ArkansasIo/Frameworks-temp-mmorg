#include "Stats/RPGStatsComponent.h"
#include "Net/UnrealNetwork.h"
#include "Algo/Sort.h"

URPGStatsComponent::URPGStatsComponent(){ SetIsReplicatedByDefault(true); }

double URPGStatsComponent::GetStat(ERPGStatId Stat) const { if(const double* V=EffectiveStats.Find(Stat)) return *V; if(const double* B=BaseStats.Find(Stat)) return *B; return 0.0; }
void URPGStatsComponent::SetBaseStat(ERPGStatId Stat,double Value){ BaseStats.FindOrAdd(Stat)=Value; RecalculateStats(); }
void URPGStatsComponent::AddModifier(const FRPGStatModifier& Modifier){ Modifiers.Add(Modifier); RecalculateStats(); }
void URPGStatsComponent::RemoveModifiersFromSource(FName SourceId){ Modifiers.RemoveAll([&](const FRPGStatModifier& M){return M.SourceId==SourceId;}); RecalculateStats(); }
void URPGStatsComponent::RecalculateStats(){ EffectiveStats=BaseStats; TArray<FRPGStatModifier> Sorted=Modifiers; Sorted.Sort([](const auto&A,const auto&B){return A.Priority<B.Priority;}); for(const auto&M:Sorted){ double& V=EffectiveStats.FindOrAdd(M.Stat); switch(M.Operation){case ERPGModifierOperation::AddFlat:V+=M.Value;break;case ERPGModifierOperation::AddPercent:V+=V*M.Value;break;case ERPGModifierOperation::Multiply:V*=M.Value;break;case ERPGModifierOperation::Override:V=M.Value;break;case ERPGModifierOperation::Minimum:V=FMath::Max(V,M.Value);break;case ERPGModifierOperation::Maximum:V=FMath::Max(V,M.Value);break;default:break;}} }
void URPGStatsComponent::GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& O) const { Super::GetLifetimeReplicatedProps(O); DOREPLIFETIME(URPGStatsComponent,BaseStats); DOREPLIFETIME(URPGStatsComponent,Modifiers); DOREPLIFETIME(URPGStatsComponent,EffectiveStats); }
