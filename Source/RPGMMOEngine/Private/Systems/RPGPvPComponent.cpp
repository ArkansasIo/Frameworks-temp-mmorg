#include "Systems/RPGPvPComponent.h"
#include "Net/UnrealNetwork.h"

void URPGPvPComponent::SetPvPEnabled(bool bEnabled)
{
    PvPState = bEnabled ? ERPGPvPState::Flagged : ERPGPvPState::Peaceful;
    if (!bEnabled) DuelTarget = nullptr;
}

bool URPGPvPComponent::RequestDuel(AActor* Target)
{
    if (!IsValid(Target) || Target == GetOwner()) return false;
    DuelTarget = Target;
    PvPState = ERPGPvPState::DuelPending;
    return true;
}

void URPGPvPComponent::AcceptDuel()
{
    if (PvPState == ERPGPvPState::DuelPending && IsValid(DuelTarget)) PvPState = ERPGPvPState::DuelActive;
}

void URPGPvPComponent::CancelDuel()
{
    DuelTarget = nullptr;
    PvPState = ERPGPvPState::Peaceful;
}

void URPGPvPComponent::GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const
{
    Super::GetLifetimeReplicatedProps(OutLifetimeProps);
    DOREPLIFETIME(URPGPvPComponent, PvPState);
    DOREPLIFETIME(URPGPvPComponent, DuelTarget);
}
