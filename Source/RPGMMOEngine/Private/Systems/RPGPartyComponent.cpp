#include "Systems/RPGPartyComponent.h"
#include "Net/UnrealNetwork.h"
bool URPGPartyComponent::AddMember(AActor* Character){if(!Character)return false;for(const auto&M:Members)if(M.Character==Character)return false;FRPGPartyMember M;M.Character=Character;M.bLeader=Members.Num()==0;Members.Add(M);return true;}
bool URPGPartyComponent::RemoveMember(AActor* Character){for(int32 i=0;i<Members.Num();++i)if(Members[i].Character==Character){Members.RemoveAt(i);if(Members.Num()>0&&!Members.ContainsByPredicate([](const FRPGPartyMember&M){return M.bLeader;}))Members[0].bLeader=true;return true;}return false;}
bool URPGPartyComponent::SetLeader(AActor* Character){bool found=false;for(FRPGPartyMember&M:Members){M.bLeader=M.Character==Character;found|=M.bLeader;}return found;}
AActor* URPGPartyComponent::GetLeader()const{for(const auto&M:Members)if(M.bLeader)return M.Character;return nullptr;}
void URPGPartyComponent::GetLifetimeReplicatedProps(TArray<FLifetimeProperty>&Out)const{Super::GetLifetimeReplicatedProps(Out);DOREPLIFETIME(URPGPartyComponent,Members);}
