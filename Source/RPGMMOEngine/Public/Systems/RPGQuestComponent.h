#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "RPGQuestComponent.generated.h"

UENUM(BlueprintType) enum class ERPGQuestState:uint8 { Inactive, Available, Active, Completed, Failed, Abandoned };
USTRUCT(BlueprintType) struct RPGMMOENGINE_API FRPGQuestState { GENERATED_BODY() UPROPERTY(BlueprintReadWrite) FName QuestId; UPROPERTY(BlueprintReadWrite) ERPGQuestState State=ERPGQuestState::Inactive; UPROPERTY(BlueprintReadWrite) int32 Progress=0; UPROPERTY(BlueprintReadWrite) int32 Required=1; };

UCLASS(ClassGroup=(RPG), BlueprintType, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGQuestComponent : public UActorComponent
{
    GENERATED_BODY()
public:
    UFUNCTION(BlueprintCallable) void StartQuest(FName QuestId,int32 RequiredProgress=1);
    UFUNCTION(BlueprintCallable) void AdvanceQuest(FName QuestId,int32 Amount=1);
    UFUNCTION(BlueprintCallable) void AbandonQuest(FName QuestId);
    UFUNCTION(BlueprintPure) bool IsQuestComplete(FName QuestId) const;
protected:
    UPROPERTY(Replicated,BlueprintReadOnly) TArray<FRPGQuestState> Quests;
};
