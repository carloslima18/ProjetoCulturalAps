<?php

namespace app\models;

use Yii;

/**
 * This is the model class for table "publicacao".
 *
 * @property integer $id
 * @property string $nome
 * @property string $redesocial
 * @property string $endereco
 * @property string $contato
 * @property string $atvexercida
 * @property string $categoria
 * @property double $latitude
 * @property double $longitude
 * @property string $geo_gps
 * @property string $img1
 * @property string $img2
 * @property string $img3
 * @property string $img4
 * @property integer $fk_user
 *
 * @property Avalicoes[] $avalicoes
 * @property Users $fkUser
 */
class Publicacao extends \yii\db\ActiveRecord
{

    /**
     * @inheritdoc
     */
    public static function tableName()
    {
        return 'publicacao';
    }

    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['nome', 'endereco', 'contato', 'atvexercida', 'categoria'], 'safe'],
            [['nome', 'redesocial', 'endereco', 'contato', 'atvexercida', 'categoria', 'geo_gps', 'img1', 'img2', 'img3', 'img4'], 'safe'],
            [['latitude', 'longitude'], 'number'],
            [['fk_user'], 'integer'],
            [['fk_user'], 'exist']//, 'skipOnError' => true, 'targetClass' => Users::className(), 'targetAttribute' => ['fk_user' => 'id']],
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
            'redesocial' => 'Redesocial',
            'endereco' => 'Endereco',
            'contato' => 'Contato',
            'atvexercida' => 'Atvexercida',
            'categoria' => 'Categoria',
            'latitude' => 'Latitude',
            'longitude' => 'Longitude',
            'geo_gps' => 'Geo Gps',
            'img1' => 'Img1',
            'img2' => 'Img2',
            'img3' => 'Img3',
            'img4' => 'Img4',
            'fk_user' => 'Fk User',
        ];
    }

}
