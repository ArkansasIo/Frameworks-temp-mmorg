#include "Systems/RPGMMOBlueprintLibrary.h"
#include "Math/UnrealMathUtility.h"
float URPGMMOBlueprintLibrary::CalculateCritMultiplier(float CritPower,float Base){return FMath::Max(1.f,Base+CritPower);}
float URPGMMOBlueprintLibrary::CalculateMitigatedDamage(float RawDamage,float Armor,float ArmorConstant){if(RawDamage<=0.f)return 0.f;return RawDamage*(ArmorConstant/(ArmorConstant+FMath::Max(0.f,Armor)));}
int64 URPGMMOBlueprintLibrary::RequiredXP(int32 Level){if(Level<=1)return 0;return FMath::FloorToInt64(FMath::Pow((double)Level,2.5)*500.0);}
