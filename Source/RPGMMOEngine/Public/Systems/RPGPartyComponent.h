#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "RPGPartyComponent.generated.h"
USTRUCT(BlueprintType) struct RPGMMOENGINE_API FRPGPartyMember { GENERATED_BODY() UPROPERTY(BlueprintReadOnly) TObjectPtr<AActor> Character; UPROPERTY(BlueprintReadOnly) bool bLeader=false; };
UCLASS(ClassGroup=(RPG),BlueprintType,meta=(BlueprintSpawnableComponent)) class RPGMMOENGINE_API URPGPartyComponent:public UActorComponent { GENERATED_BODY() public: UFUNCTION(BlueprintCallable) bool AddMember(AActor* Character); UFUNCTION(BlueprintCallable) bool RemoveMember(AActor* Character); UFUNCTION(BlueprintCallable) bool SetLeader(AActor* Character); UFUNCTION(BlueprintPure) int32 GetMemberCount()const{return Members.Num();} UFUNCTION(BlueprintPure) AActor* GetLeader()const; protected: UPROPERTY(Replicated,BlueprintReadOnly) TArray<FRPGPartyMember> Members; virtual void GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& Out)const override; };
