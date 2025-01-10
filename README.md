# BuildAI
## Functionality
BuildAI is a creative mode utility minecraft plungin that allows various Large Language Models to build structures ingame. This plugin is just for fun, **AI models are generally not advanced enough to build anything good**. To use this plugin, I suggest [Ollama](https://ollama.com/) to run them, as this allows you to run LLMs on your pc for free.
## Configuration
You can configure the prompt and add your own models from most providers. BuildAI allows you to configure a custom payload and use variables for the users to set. Ollama is preconfigured, so you just have to download it and run `ollama pull gemma2:latest` in your terminal _([Gemma2](https://ollama.com/library/gemma2) was a model which was able to follow the prompt reasonably well)_
## Usage
As an operator, you can use the command:  
`/aigenerate {model_preset} {x1} {y1} {z1} {x2} {y2} {z2} {somevar=somevalue} {prompt}`. If you have [WorldEdit](https://modrinth.com/plugin/worldedit) installed on the server, you can just make a selection and use the command:   
`/aigenerate {model_preset} {somevar=somevalue} {prompt}`. The variables ("somevar") can be configured in the config.
## Security
Most Providers require an API-Key to use the models. Storing this key safely is very important, as everybody with this key can use the model without your knowledge. This plugin provides 2 main ways of storing api-keys:
1. **Storing the key in the config.yml file:** While players can not see the key this way, you should still think about who else could have access to the config. Also, this command has no spam protection, so be careful who gets access to the command.
2. **Using %api-key% in the config and letting the players input their own key:** It is very important to know that anyone with access to the terminal can see the api-key. For players, it is not recommended to input your api key if you do not own the server
## Verisons
BuildAI was developed with [Paper](https://papermc.io/downloads/paper) 1.20.1 and is primarly tested with this version. It should be able to run on all bukkit based softwares in all versions after 1.13
## Official links
- Modrinth: [https://modrinth.com/project/buildai](https://modrinth.com/project/buildai)
- PaperMC Hangar: [https://hangar.papermc.io/KEL0002/BuildAI](https://hangar.papermc.io/KEL0002/BuildAI)
- Source: [https://github.com/KEL0002/BuildAI](https://github.com/KEL0002/BuildAI)
