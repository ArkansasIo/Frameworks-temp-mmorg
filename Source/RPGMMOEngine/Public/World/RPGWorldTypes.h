#pragma once

#include "CoreMinimal.h"
#include "RPGWorldTypes.generated.h"

USTRUCT(BlueprintType)
struct RPGMMOENGINE_API FRPGWorldRoom
{
    GENERATED_BODY()

    UPROPERTY(EditAnywhere, BlueprintReadWrite) int32 RoomId = 0;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) FName ZoneId;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) FText Name;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) FText Description;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) TMap<FName, int32> Exits;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) bool bSafe = false;
};

USTRUCT(BlueprintType)
struct RPGMMOENGINE_API FRPGZoneDefinition
{
    GENERATED_BODY()

    UPROPERTY(EditAnywhere, BlueprintReadWrite) FName ZoneId;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) int32 MinimumRoomId = 0;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) int32 MaximumRoomId = 0;
    UPROPERTY(EditAnywhere, BlueprintReadWrite) bool bEnabled = true;
};
