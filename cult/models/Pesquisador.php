<?php

namespace app\models;

use Yii;

/**
 * This is the model class for table "pesquisador".
 *
 * @property int $id
 * @property string $nome
 * @property string $email
 * @property string $senha
 * @property string $campo1
 *
 * @property Publicacaopesq[] $publicacaopesqs
 */
class Pesquisador extends \yii\db\ActiveRecord
{
    /**
     * @inheritdoc
     */
    public static function tableName()
    {
        return 'pesquisador';
    }

    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['nome', 'email', 'senha', 'campo1'], 'string'],
        ];
    }

    /**
     * @inheritdoc
     */
    public function attributeLabels()
    {
        return [
            'id' => 'ID',
            'nome' => 'Nome',
            'email' => 'Email',
            'senha' => 'Senha',
           // 'campo1' => 'Campo1',
        ];
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getPublicacaopesqs()
    {
        return $this->hasMany(Publicacaopesq::className(), ['pesquisador' => 'id']);
    }
}
