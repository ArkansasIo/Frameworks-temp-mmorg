#pragma once
#include "CoreMinimal.h"
#include "Kismet/BlueprintFunctionLibrary.h"
#include "RPGMMOBlueprintLibrary.generated.h"
UCLASS()
class RPGMMOENGINE_API URPGMMOBlueprintLibrary:public UBlueprintFunctionLibrary{GENERATED_BODY()public:UFUNCTION(BlueprintPure,Category="RPG|Math")static float CalculateCritMultiplier(float CritPower,float Base=1.5f);UFUNCTION(BlueprintPure,Category="RPG|Math")static float CalculateMitigatedDamage(float RawDamage,float Armor,float ArmorConstant=100.f);UFUNCTION(BlueprintPure,Category="RPG|Math")static int64 RequiredXP(int32 Level);};
