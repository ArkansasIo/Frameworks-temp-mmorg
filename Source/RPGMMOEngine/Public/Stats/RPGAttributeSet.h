#pragma once

#include "CoreMinimal.h"
#include "UObject/Object.h"
#include "RPGAttributeSet.generated.h"

USTRUCT(BlueprintType)
struct RPGMMOENGINE_API FRPGAttributeSet
{
    GENERATED_BODY()

    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Attributes") float Strength = 10.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Attributes") float Dexterity = 10.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Attributes") float Constitution = 10.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Attributes") float Intelligence = 10.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Attributes") float Wisdom = 10.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Attributes") float Vitality = 10.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Attributes") float Spirit = 10.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Attributes") float Luck = 10.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Attributes") float Willpower = 10.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Attributes") float Charisma = 10.0f;

    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Derived") float MaxHealth = 250.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Derived") float MaxMana = 100.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Derived") float AttackPower = 10.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Derived") float Defense = 10.0f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Derived") float CriticalChance = 0.05f;
    UPROPERTY(EditAnywhere, BlueprintReadWrite, Category="Derived") float MoveSpeed = 600.0f;

    void Recalculate()
    {
        MaxHealth = FMath::Max(1.0f, Vitality * 15.0f + Constitution * 10.0f);
        MaxMana = FMath::Max(0.0f, Intelligence * 8.0f + Spirit * 4.0f);
        AttackPower = FMath::Max(1.0f, Strength * 2.0f + Dexterity);
        Defense = FMath::Max(0.0f, Constitution + Dexterity * 0.5f);
        CriticalChance = FMath::Clamp(0.05f + Dexterity * 0.0025f + Luck * 0.0025f, 0.0f, 0.75f);
        MoveSpeed = FMath::Clamp(500.0f + Dexterity * 5.0f, 250.0f, 900.0f);
    }
};
