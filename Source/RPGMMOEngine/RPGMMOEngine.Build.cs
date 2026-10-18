using UnrealBuildTool;
using System.IO;

public class RPGMMOEngine : ModuleRules
{
    public RPGMMOEngine(ReadOnlyTargetRules Target) : base(Target)
    {
        PCHUsage = PCHUsageMode.UseExplicitOrSharedPCHs;
        PublicDependencyModuleNames.AddRange(new[] { "Core", "CoreUObject", "Engine", "NetCore" });
        PrivateDependencyModuleNames.AddRange(new[] { "NetCore" });
        PublicIncludePaths.Add(Path.Combine(ModuleDirectory, "Public"));
    }
}
