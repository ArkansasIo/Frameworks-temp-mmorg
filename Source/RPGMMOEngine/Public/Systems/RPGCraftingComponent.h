#pragma once
#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "RPGCraftingComponent.generated.h"
USTRUCT(BlueprintType) struct RPGMMOENGINE_API FRPGRecipeIngredient{GENERATED_BODY()UPROPERTY(EditAnywhere,BlueprintReadWrite)FName ItemId;UPROPERTY(EditAnywhere,BlueprintReadWrite)int32 Quantity=1;};
USTRUCT(BlueprintType) struct RPGMMOENGINE_API FRPGRecipe{GENERATED_BODY()UPROPERTY(EditAnywhere,BlueprintReadWrite)FName RecipeId;UPROPERTY(EditAnywhere,BlueprintReadWrite)TArray<FRPGRecipeIngredient> Ingredients;UPROPERTY(EditAnywhere,BlueprintReadWrite)FName ResultItemId;UPROPERTY(EditAnywhere,BlueprintReadWrite)int32 ResultQuantity=1;};
UCLASS(ClassGroup=(RPG),BlueprintType,meta=(BlueprintSpawnableComponent))class RPGMMOENGINE_API URPGCraftingComponent:public UActorComponent{GENERATED_BODY()public:UFUNCTION(BlueprintCallable)bool Craft(const FRPGRecipe& Recipe);UFUNCTION(BlueprintCallable)void RegisterRecipe(const FRPGRecipe& Recipe);UFUNCTION(BlueprintPure)bool HasRecipe(FName RecipeId)const;protected:UPROPERTY(EditAnywhere,BlueprintReadOnly)TArray<FRPGRecipe> Recipes;};
