#pragma once
#include "CoreMinimal.h"
#include "Subsystems/GameInstanceSubsystem.h"
#include "RPGPersistenceSubsystem.generated.h"
UCLASS()
class RPGMMOENGINE_API URPGPersistenceSubsystem:public UGameInstanceSubsystem{GENERATED_BODY()public:UFUNCTION(BlueprintCallable)bool SaveCharacter(FName CharacterId,const FString& Json);UFUNCTION(BlueprintCallable)bool LoadCharacter(FName CharacterId,FString& Json)const;private:UPROPERTY()TMap<FName,FString> CharacterData;};
