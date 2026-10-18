#include "Systems/RPGQuestComponent.h"
#include "Net/UnrealNetwork.h"

URPGQuestComponent::URPGQuestComponent()
{
    SetIsReplicatedByDefault(true);
}

void URPGQuestComponent::StartQuest(FName QuestId, int32 RequiredProgress)
{
    if (QuestId.IsNone()) return;
    for (FRPGQuestState& Quest : Quests)
    {
        if (Quest.QuestId == QuestId)
        {
            Quest.State = ERPGQuestState::Active;
            Quest.Progress = 0;
            Quest.Required = FMath::Max(1, RequiredProgress);
            return;
        }
    }
    FRPGQuestState NewQuest;
    NewQuest.QuestId = QuestId;
    NewQuest.State = ERPGQuestState::Active;
    NewQuest.Required = FMath::Max(1, RequiredProgress);
    Quests.Add(NewQuest);
}

void URPGQuestComponent::AdvanceQuest(FName QuestId, int32 Amount)
{
    if (Amount <= 0) return;
    for (FRPGQuestState& Quest : Quests)
    {
        if (Quest.QuestId == QuestId && Quest.State == ERPGQuestState::Active)
        {
            Quest.Progress = FMath::Clamp(Quest.Progress + Amount, 0, Quest.Required);
            if (Quest.Progress >= Quest.Required) Quest.State = ERPGQuestState::Completed;
            return;
        }
    }
}

void URPGQuestComponent::AbandonQuest(FName QuestId)
{
    for (FRPGQuestState& Quest : Quests)
    {
        if (Quest.QuestId == QuestId && Quest.State == ERPGQuestState::Active)
        {
            Quest.State = ERPGQuestState::Abandoned;
            return;
        }
    }
}

bool URPGQuestComponent::IsQuestComplete(FName QuestId) const
{
    const FRPGQuestState* Quest = Quests.FindByPredicate([QuestId](const FRPGQuestState& Item)
    {
        return Item.QuestId == QuestId;
    });
    return Quest && Quest->State == ERPGQuestState::Completed;
}

void URPGQuestComponent::GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const
{
    Super::GetLifetimeReplicatedProps(OutLifetimeProps);
    DOREPLIFETIME(URPGQuestComponent, Quests);
}
