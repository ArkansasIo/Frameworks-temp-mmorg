#pragma once

#include "CoreMinimal.h"
#include "GameFramework/Actor.h"
#include "UObject/Object.h"
#include "RPGCommandRouter.generated.h"

DECLARE_DYNAMIC_DELEGATE_RetVal_TwoParams(bool, FRPGCommandHandler, AActor*, Executor, const FString&, Arguments);

UCLASS(BlueprintType)
class RPGMMOENGINE_API URPGCommandRouter : public UObject
{
    GENERATED_BODY()

public:
    UFUNCTION(BlueprintCallable, Category="RPG|Commands") bool RegisterCommand(FName Command, const FRPGCommandHandler& Handler);
    UFUNCTION(BlueprintCallable, Category="RPG|Commands") bool ExecuteCommand(AActor* Executor, const FString& Input);
    UFUNCTION(BlueprintPure, Category="RPG|Commands") bool HasCommand(FName Command) const;
    UFUNCTION(BlueprintCallable, Category="RPG|Commands") void ClearCommands();

private:
    TMap<FName, FRPGCommandHandler> Commands;
};
