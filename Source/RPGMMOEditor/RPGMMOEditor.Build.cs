using UnrealBuildTool;

public class RPGMMOEditor : ModuleRules
{
    public RPGMMOEditor(ReadOnlyTargetRules Target) : base(Target)
    {
        PCHUsage = PCHUsageMode.UseExplicitOrSharedPCHs;
        PublicDependencyModuleNames.AddRange(new[] { "Core", "CoreUObject", "Engine", "RPGMMOEngine" });
        PrivateDependencyModuleNames.AddRange(new[] { "UnrealEd", "Slate", "SlateCore", "EditorSubsystem" });
    }
}
