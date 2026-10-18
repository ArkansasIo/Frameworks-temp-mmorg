#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "RPGTalentComponent.generated.h"

USTRUCT(BlueprintType)
struct RPGMMOENGINE_API FRPGTalentNode
{
    GENERATED_BODY()
    UPROPERTY(EditAnywhere, BlueprintReadWrite) FName TalentId;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) FName ParentTalentId;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) int32 Cost = 1;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) bool bUnlocked = false;
};

UCLASS(ClassGroup=(RPG), BlueprintType, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGTalentComponent : public UActorComponent
{
    GENERATED_BODY()
public:
    UFUNCTION(BlueprintCallable) bool UnlockTalent(FName TalentId);
    UFUNCTION(BlueprintCallable) void RegisterTalent(const FRPGTalentNode& Talent);
    UFUNCTION(BlueprintPure) bool IsTalentUnlocked(FName TalentId) const;
    UFUNCTION(BlueprintPure) int32 GetTalentPoints() const { return TalentPoints; }
    UFUNCTION(BlueprintCallable) void AddTalentPoints(int32 Amount);
protected:
    UPROPERTY(Replicated, EditAnywhere, BlueprintReadOnly) TArray<FRPGTalentNode> Talents;
    UPROPERTY(Replicated, EditAnywhere, BlueprintReadOnly) int32 TalentPoints = 0;
    virtual void GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const override;
};
