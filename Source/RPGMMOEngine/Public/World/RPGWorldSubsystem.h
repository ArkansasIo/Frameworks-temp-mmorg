#pragma once

#include "CoreMinimal.h"
#include "Subsystems/WorldSubsystem.h"
#include "World/RPGWorldTypes.h"
#include "RPGWorldSubsystem.generated.h"

DECLARE_DYNAMIC_MULTICAST_DELEGATE_TwoParams(FRPGRoomChangedEvent, int32, PreviousRoomId, int32, NewRoomId);

UCLASS(BlueprintType)
class RPGMMOENGINE_API URPGWorldSubsystem : public UWorldSubsystem
{
    GENERATED_BODY()

public:
    UFUNCTION(BlueprintCallable, Category="RPG|World") bool RegisterRoom(const FRPGWorldRoom& Room);
    UFUNCTION(BlueprintCallable, Category="RPG|World") bool RemoveRoom(int32 RoomId);
    UFUNCTION(BlueprintPure, Category="RPG|World") bool GetRoom(int32 RoomId, FRPGWorldRoom& OutRoom) const;
    UFUNCTION(BlueprintPure, Category="RPG|World") bool CanMove(int32 RoomId, FName Direction) const;
    UFUNCTION(BlueprintPure, Category="RPG|World") int32 GetDestination(int32 RoomId, FName Direction) const;
    UFUNCTION(BlueprintCallable, Category="RPG|World") bool MoveCharacter(AActor* Character, FName Direction);

    UPROPERTY(BlueprintAssignable, Category="RPG|World") FRPGRoomChangedEvent OnRoomChanged;

protected:
    UPROPERTY() TMap<int32, FRPGWorldRoom> Rooms;
    UPROPERTY() TMap<TObjectPtr<AActor>, int32> CharacterRooms;
};
