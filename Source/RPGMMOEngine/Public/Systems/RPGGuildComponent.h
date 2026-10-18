#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "RPGGuildComponent.generated.h"

USTRUCT(BlueprintType)
struct RPGMMOENGINE_API FRPGGuildMember
{
    GENERATED_BODY()
    UPROPERTY(BlueprintReadOnly) TObjectPtr<AActor> Character;
    UPROPERTY(BlueprintReadOnly) bool bOfficer = false;
};

UCLASS(ClassGroup=(RPG), BlueprintType, meta=(BlueprintSpawnableComponent))
class RPGMMOENGINE_API URPGGuildComponent : public UActorComponent
{
    GENERATED_BODY()
public:
    UFUNCTION(BlueprintCallable, Category="RPG|Guild") bool AddMember(AActor* Character, bool bOfficer = false);
    UFUNCTION(BlueprintCallable, Category="RPG|Guild") bool RemoveMember(AActor* Character);
    UFUNCTION(BlueprintCallable, Category="RPG|Guild") bool SetOfficer(AActor* Character, bool bOfficer);
    UFUNCTION(BlueprintPure, Category="RPG|Guild") bool IsMember(AActor* Character) const;
    UFUNCTION(BlueprintPure, Category="RPG|Guild") int32 GetMemberCount() const;
protected:
    UPROPERTY(Replicated, BlueprintReadOnly, Category="RPG|Guild") TArray<FRPGGuildMember> Members;
    virtual void GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const override;
};
