#include "Modules/ModuleManager.h"

class FRPGMMOEditorModule : public IModuleInterface
{
public:
    virtual void StartupModule() override {}
    virtual void ShutdownModule() override {}
};

IMPLEMENT_MODULE(FRPGMMOEditorModule, RPGMMOEditor)
