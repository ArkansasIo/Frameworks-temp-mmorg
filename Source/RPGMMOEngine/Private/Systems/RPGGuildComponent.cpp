#include "Systems/RPGGuildComponent.h"
#include "Net/UnrealNetwork.h"

bool URPGGuildComponent::AddMember(AActor* Character, bool bOfficer)
{
    if (!Character || IsMember(Character)) return false;
    FRPGGuildMember Member; Member.Character = Character; Member.bOfficer = bOfficer; Members.Add(Member); return true;
}

bool URPGGuildComponent::RemoveMember(AActor* Character) { return Character && Members.RemoveAll([Character](const FRPGGuildMember& M){ return M.Character == Character; }) > 0; }

bool URPGGuildComponent::SetOfficer(AActor* Character, bool bOfficer)
{
    for (FRPGGuildMember& Member : Members) if (Member.Character == Character) { Member.bOfficer = bOfficer; return true; }
    return false;
}

bool URPGGuildComponent::IsMember(AActor* Character) const { for (const FRPGGuildMember& Member : Members) if (Member.Character == Character) return true; return false; }
int32 URPGGuildComponent::GetMemberCount() const { return Members.Num(); }

void URPGGuildComponent::GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const
{
    Super::GetLifetimeReplicatedProps(OutLifetimeProps);
    DOREPLIFETIME(URPGGuildComponent, Members);
}
