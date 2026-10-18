#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "RPGFactionComponent.generated.h"

USTRUCT(BlueprintType)
struct RPGMMOENGINE_API FRPGFactionStanding
{
    GENERATED_BODY()
    UPROPERTY(EditAnywhere, BlueprintReadWrite) FName FactionId;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) int32 Standing = 0;
};

UCLASS(ClassGroup=(RPG), BlueprintType, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGFactionComponent : public UActorComponent
{
    GENERATED_BODY()
public:
    UFUNCTION(BlueprintCallable, Category="RPG|Faction") void SetStanding(FName FactionId, int32 Standing);
    UFUNCTION(BlueprintCallable, Category="RPG|Faction") void ModifyStanding(FName FactionId, int32 Delta);
    UFUNCTION(BlueprintPure, Category="RPG|Faction") int32 GetStanding(FName FactionId) const;
    UFUNCTION(BlueprintPure, Category="RPG|Faction") bool IsHostile(FName FactionId, int32 HostileThreshold = -500) const;
protected:
    UPROPERTY(EditAnywhere, BlueprintReadOnly, Category="RPG|Faction") TArray<FRPGFactionStanding> Standings;
};
