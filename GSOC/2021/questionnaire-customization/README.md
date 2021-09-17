<img src="https://user-images.githubusercontent.com/62053304/128657744-924b1f39-05cd-46a9-8f4d-2412ec6f38af.png" align="center"/>

# Google Summer of Code 2021 Work Product

**Student** : [Fikri Yurcel Milano](https://github.com/fikrimilano)

**Project** : [Create Custom Styles in SDC Gallery](https://summerofcode.withgoogle.com/projects/#6154675767738368)

**Organization** : [Google FHIR SDK](https://summerofcode.withgoogle.com/organizations/6201077587771392)

**Mentors** : [Simon Njoroge](https://github.com/qiarie), [Ronald Kudoyi](https://github.com/rkodev)

## Create Custom Styles in SDC Gallery

<img src="https://user-images.githubusercontent.com/62053304/129698136-81612c61-db42-4709-a677-efdf0a2d38ea.png" width="192"> <img src="https://user-images.githubusercontent.com/62053304/129698533-a7621b3d-96c0-4540-b01c-85562f942463.png" width="192"> <img src="https://user-images.githubusercontent.com/62053304/129698922-1bab23d0-6d15-44ca-92a7-99d990786747.png" width="192">

The SDC Library helps implementers to render their questionnaires. When implementers use the SDC Library on their application, they may want to customize their questionnaires on how they will look. But the current version of SDC Library doesn't support questionnaire customization. Implementers also needs an example and documentation on how to customize their questionnaire.

By working on this project, the SDC Library will be able to support questionnaire customization so that implementers can change their questionnaire color/style/text appearance to match their application. This project also provides the SDC Gallery App as an example and documentation as a guide on how to customize the questionnaire.

**Issue** : [#501: Create examples of custom style in the sdc gallery application](https://github.com/google/android-fhir/issues/501)

**Pull Request** : [#633: Create examples of custom style in SDC Gallery](https://github.com/google/android-fhir/pull/633)

**Commits** : [Custom Styles in SDC Gallery](https://github.com/google/android-fhir/pull/633/commits)

### What work was done

* In SDC Library
  * Create attributes for views in each questionnaire
  * Assign default style for each attribute
* In SDC Gallery App
  * Create custom style
  * Re-assign each attribute with the custom style

### To do

* Merge the PR (Currently waiting for review)

### Guides to customize

The principle to customize is simple, just override the available attributes in your app `styles.xml` and assign them with your own custom style.

#### Atrributes

| Attributes                     | Default Style                                                             |
| :----------------------------- | :------------------------------------------------------------------------ |
| groupHeaderTextAppearance      | TextAppearance.MaterialComponents.Headline6                               |
| headerTextAppearance           | TextAppearance.MaterialComponents.Subtitle2                               |
| checkBoxStyle                  | Widget.AppCompat.CompoundButton.CheckBox                                  |
| radioButtonStyle               | Widget.AppCompat.CompoundButton.RadioButton                               |
| dropDownTextAppearance         | TextAppearance.MaterialComponents.Subtitle1                               |
| dropDownSelectedTextAppearance | TextAppearance.MaterialComponents.Subtitle1                               |
| dropDownStyle                  | Widget.MaterialComponents.TextInputLayout.OutlinedBox.ExposedDropdownMenu |
| editTextInputTextAppearance    | TextAppearance.MaterialComponents.Subtitle1                               |
| editTextLayoutStyle            | Widget.MaterialComponents.TextInputLayout.OutlinedBox                     |
| chipStyle                      | Widget.MaterialComponents.Chip.Entry                                      |

#### Examples of customization
Do this in your app `styles.xml`.

1. Group Header

<img src="https://user-images.githubusercontent.com/62053304/129699084-0559861a-cf49-4762-8dfe-a7c9116171a8.png" width="512">

```
<resources>
    <style name="Theme.MyQuestionnaire" parent="Theme.Questionnaire">
        <item
            name="groupHeaderTextAppearance"
        >@style/TextAppearance.MyQuestionnaire.GroupHeader</item>
    </style>
    
    // Custom style
    <style
        name="TextAppearance.MyQuestionnaire.GroupHeader"
        parent="TextAppearance.MaterialComponents.Headline6"
    >
        <item name="android:textColor">@color/purple_700</item>
    </style>
</resources>
```

2. Header

<img src="https://user-images.githubusercontent.com/62053304/129701274-7c0d6b8a-dc9e-48f2-bc09-fb2ae9932c47.png" width="512">

```
<resources>
    <style name="Theme.MyQuestionnaire" parent="Theme.Questionnaire">
        <item
            name="headerTextAppearance"
        >@style/TextAppearance.MyQuestionnaire.Header</item>
    </style>
    
    // Custom style
    <style
        name="TextAppearance.MyQuestionnaire.Header"
        parent="TextAppearance.MaterialComponents.Subtitle2"
    >
        <item name="android:textColor">?attr/colorOnSurface</item>
    </style>
</resources>
```



3. Checkbox

<img src="https://user-images.githubusercontent.com/62053304/129699453-0ba3efff-07ce-4718-ae1c-08bf3240823f.png" width="512">

```
<resources>
    <style name="Theme.MyQuestionnaire" parent="Theme.Questionnaire">
        <item name="checkBoxStyle">@style/Widget.MyQuestionnaire.CheckBox</item>
    </style>
    
    // Custom style
    <style
        name="Widget.MyQuestionnaire.CheckBox"
        parent="Widget.AppCompat.CompoundButton.CheckBox"
    >
        <item name="android:buttonTint">@color/check_box_tint</item>
        <item name="android:textColor">?attr/colorOnSurface</item>
        <item
            name="android:textAppearance"
        >@style/TextAppearance.AppCompat</item>
    </style>
</resources>
```


4. Radio Button

<img src="https://user-images.githubusercontent.com/62053304/129699584-d5a49930-8b64-41e1-a5ca-407437b8b57f.png" width="512">

```
<resources>
    <style name="Theme.MyQuestionnaire" parent="Theme.Questionnaire">
        <item
            name="radioButtonStyle"
        >@style/Widget.MyQuestionnaire.RadioButton</item>
    </style>
    
    // Custom style
    <style
        name="Widget.MyQuestionnaire.RadioButton"
        parent="Widget.AppCompat.CompoundButton.RadioButton"
    >
        <item name="android:buttonTint">@color/radio_button_tint</item>
        <item name="android:textColor">?attr/colorOnSurface</item>
        <item
            name="android:textAppearance"
        >@style/TextAppearance.AppCompat</item>
    </style>
</resources>
```


5. Drop Down

<img src="https://user-images.githubusercontent.com/62053304/129700679-4b05ab2d-dfcf-4a00-8c5f-e61b72ac4550.png" width="512">

```
<resources>
    <style name="Theme.MyQuestionnaire" parent="Theme.Questionnaire">
        <item
            name="dropDownTextAppearance"
        >@style/TextAppearance.MyQuestionnaire.DropDown</item>
        <item
            name="dropDownSelectedTextAppearance"
        >@style/TextAppearance.MyQuestionnaire.DropDownSelected</item>
        <item name="dropDownStyle">@style/Widget.MyQuestionnaire.DropDown</item>
    </style>
    
    // Custom style
    <style
        name="TextAppearance.MyQuestionnaire.DropDown"
        parent="TextAppearance.MaterialComponents.Subtitle1"
    >
        <item name="android:textColor">?attr/colorOnSurface</item>
    </style>

    <style
        name="TextAppearance.MyQuestionnaire.DropDownSelected"
        parent="TextAppearance.MaterialComponents.Subtitle1"
    >
        <item name="android:textColor">?attr/colorOnSurface</item>
    </style>
    
    <style
        name="Widget.MyQuestionnaire.DropDown"
        parent="Widget.MaterialComponents.TextInputLayout.OutlinedBox.ExposedDropdownMenu"
    />
</resources>
```


6. Edit Text

<img src="https://user-images.githubusercontent.com/62053304/129700770-f4392609-7189-4c03-9ce7-5c27cfd14f80.png" width="512">

```
<resources>
    <style name="Theme.MyQuestionnaire" parent="Theme.Questionnaire">
        <item
            name="editTextInputTextAppearance"
        >@style/TextAppearance.MyQuestionnaire.EditTextInput</item>
        <item
            name="editTextLayoutStyle"
        >@style/Widget.MyQuestionnaire.EditText</item>
    </style>
    
    // Custom style
    <style
        name="TextAppearance.MyQuestionnaire.EditTextInput"
        parent="TextAppearance.MaterialComponents.Subtitle1"
    >
        <item name="android:textColor">?attr/colorOnSurface</item>
    </style>
    
    <style
        name="Widget.MyQuestionnaire.EditText"
        parent="Widget.MaterialComponents.TextInputLayout.OutlinedBox"
    />
</resources>
```


7. Chip

<img src="https://user-images.githubusercontent.com/62053304/129699974-21cd55c5-40c8-460a-a1f9-2f0403c391ca.png" width="512">

```
<resources>
    <style name="Theme.MyQuestionnaire" parent="Theme.Questionnaire">
        <item name="chipStyle">@style/Widget.MyQuestionnaire.Chip</item>
    </style>
    
    // Custom style
    <style
        name="Widget.MyQuestionnaire.Chip"
        parent="Widget.MaterialComponents.Chip.Entry"
    >
        <item name="chipIconTint">@color/mtrl_chip_close_icon_tint</item>
        <item
            name="chipBackgroundColor"
        >@color/mtrl_chip_background_color</item>
        <item name="chipStrokeWidth">0dp</item>
        <item name="chipStrokeColor">?attr/colorOnSurface</item>
        <item name="android:textAppearance">?attr/textAppearanceBody2</item>
    </style>
</resources>
```

## The state of the library before and after the project

Before this project, the questionnaires could only use it's own view default style with no customization. After the project, implementers could custom each view of the questionnaire through attributes provided by the SDC Library.