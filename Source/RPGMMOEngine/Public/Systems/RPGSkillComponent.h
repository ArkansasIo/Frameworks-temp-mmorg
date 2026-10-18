#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "RPGSkillComponent.generated.h"

USTRUCT(BlueprintType)
struct RPGMMOENGINE_API FRPGSkillState
{
    GENERATED_BODY()
    UPROPERTY(EditAnywhere, BlueprintReadWrite) FName SkillId;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) int32 Rank = 0;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) int32 MaxRank = 10;
};

UCLASS(ClassGroup=(RPG), BlueprintType, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGSkillComponent : public UActorComponent
{
    GENERATED_BODY()
public:
    UFUNCTION(BlueprintCallable, Category="RPG|Skills") bool LearnSkill(FName SkillId, int32 Rank = 1);
    UFUNCTION(BlueprintCallable, Category="RPG|Skills") bool AddSkillRank(FName SkillId, int32 Amount = 1);
    UFUNCTION(BlueprintPure, Category="RPG|Skills") int32 GetSkillRank(FName SkillId) const;
    UFUNCTION(BlueprintPure, Category="RPG|Skills") bool HasSkill(FName SkillId, int32 RequiredRank = 1) const;
protected:
    UPROPERTY(EditAnywhere, BlueprintReadOnly, Category="RPG|Skills") TArray<FRPGSkillState> Skills;
};
